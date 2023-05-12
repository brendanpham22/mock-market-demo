package com.ascertain.mockdemo.config;

import com.ascertain.mockdemo.entity.Product;
import com.ascertain.mockdemo.service.DataService;
import com.ascertain.mockdemo.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
@AllArgsConstructor
public class FileListener implements FileChangeListener {
    private final DataService dataService;
    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for(ChangedFiles cfiles : changeSet) {
            for(ChangedFile cfile: cfiles.getFiles()) {
                if( cfile.getType().equals(ChangedFile.Type.MODIFY)) {
                    File file = cfile.getFile();
                    if(file.exists() && !file.isDirectory()){
                        String jsonString = JsonUtil.loadJson(file.getPath());
                        dataService.syncData(Product.buildProductsFromJson(new JSONObject(jsonString)));
                    }
                }
            }
        }
    }
}
