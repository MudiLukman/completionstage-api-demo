package com.kontrol.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PageParam {
    @QueryParam("pageNumber") @DefaultValue("0") public int pageNumber;
    @QueryParam("pageSize") @DefaultValue("10") public int pageSize;
}
