package com.github.microcatalog.service.dto.custom;

import java.io.Serializable;

public final class MicroserviceDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 4624953286552443974L;

    private String ciUrl;

    public String getCiUrl() {
        return ciUrl;
    }

    public void setCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
    }
}
