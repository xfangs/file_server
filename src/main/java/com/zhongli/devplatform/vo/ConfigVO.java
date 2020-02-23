package com.zhongli.devplatform.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigVO implements Serializable {

    private static final long serialVersionUID = -1307847681393763896L;

    private Long id;

    private String baseDir;

    private String fileServerUrl;

    private String defPassword;


}
