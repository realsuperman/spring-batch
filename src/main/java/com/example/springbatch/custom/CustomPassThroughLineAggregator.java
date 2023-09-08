package com.example.springbatch.custom;

import com.example.springbatch.dto.OneDto;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomPassThroughLineAggregator<T> implements LineAggregator<T> {
    @Override
    public String aggregate(T item) {
        if(item instanceof OneDto){
            return item.toString()+"_item";
        }
        return item.toString();
    }
}
