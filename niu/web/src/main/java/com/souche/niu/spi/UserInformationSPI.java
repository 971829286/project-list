package com.souche.niu.spi;

import com.souche.adapter.search.common.SearchQuery;
import com.souche.adapter.search.common.SearchResponse;

public interface UserInformationSPI {

    SearchResponse search(SearchQuery query, String indexName);

    int getNewVistor(String userId,String shopCode);

}
