package com.souche.niu.dao.workTable;

import com.souche.niu.model.workTable.UserCoinDo;

public interface WorkTableDao {

    UserCoinDo getCoin(String phone);
}
