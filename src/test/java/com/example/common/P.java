package com.example.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import io.github.luminion.sqlbooster.model.BPage;
import lombok.Data;

import java.util.List;

/**
 * @author luminion
 */
@Data
public class P<T> {
    private List<T> records;

    public P(IPage<T> data) {
        records = data.getRecords();
    }

    public P(BPage<T> data) {
        records = data.getRecords();
    }

    public P(PageInfo<T> pageInfo) {
        records = pageInfo.getList();
    }


    public static <T> P<T> of(IPage<T> page) {
        return new P<>(page);
    }

    public static <T> P<T> of(BPage<T> page) {
        return new P<>(page);
    }

    public static <T> P<T> of(PageInfo<T> pageInfo) {
        return new P<>(pageInfo);
    }

}
