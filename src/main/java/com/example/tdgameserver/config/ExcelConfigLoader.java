package com.example.tdgameserver.config;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExcelConfigLoader {
    private static final String CONFIG_PATH = "classpath:game-config/*.xlsx";

    @PostConstruct
    public void loadAllConfigs(){
        try{
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(CONFIG_PATH);

            for (Resource resource : resources){
                String fileName = resource.getFilename();
                if(fileName == null){
                    continue;
                }

                String tableName = fileName.substring(0,fileName.lastIndexOf("."));
                loadExcelConfig(tableName,resource.getInputStream());
            }

        }catch(IOException e){
            log.error("加载配置表失败",e);
        }
    }

    private void loadExcelConfig(String tableName, InputStream inputStream){
        try(Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            //读取字段名
            Row headerRow = sheet.getRow(0);
            if(headerRow == null){
                return;
            }

            //读取字段类型
            Row typeRow = sheet.getRow(1);
            if(typeRow == null){
                return;
            }

            //获取所有字段名
            List<String> fieldNames = new ArrayList<>();
            List<String> fieldTypes = new ArrayList<>();

            for(Cell cell : headerRow){
                fieldNames.add(cell.getStringCellValue());
            }

            for(Cell cell : typeRow){
                fieldTypes.add(cell.getStringCellValue().toLowerCase());
            }

            //读取数据行
            List<ConfigData> configList = new ArrayList<>();
            for(int i = 2;i <= sheet.getLastRowNum();i++){
                Row dataRow = sheet.getRow(i);
                if(dataRow == null){
                    continue;
                }

                Map<String,Object> data = new HashMap<>();
                int id = 0;

                for(int j = 0;j < fieldNames.size();j++){
                    Cell cell = dataRow.getCell(j);
                    String fieldName = fieldNames.get(j);
                    String fieldType = fieldTypes.get(j);

                    if(cell == null){
                        continue;
                    }

                    //根据Excel单元格类型获取值
                    Object value = getCellValue(cell);

                    //如果是第一列,设置为ID
                    if(j == 0){
                        if(value instanceof Number){
                            id = ((Number)value).intValue();
                        }
                    }

                    data.put(fieldName,value);
                }
                ConfigData configData = new ConfigData(id,tableName,data);
                configList.add(configData);
            }
            //将配置数据加载到ConfigManager中
            ConfigManager.getInstance().loadConfig(tableName,configList);

        }catch(IOException e){
            log.error("加载配置表失败",e);
        }
    }

    private Object getCellValue(Cell cell){
        switch (cell.getCellType()){
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }


}
