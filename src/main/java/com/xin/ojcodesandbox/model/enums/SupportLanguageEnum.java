package com.xin.ojcodesandbox.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description 当前代码沙箱支持的编程语言
 * @Author ZZX
 * @Date 2024/1/19 13:48
 */
public enum SupportLanguageEnum
{
    JAVA("Java", "java"), PYTHON3("Python3", "python");

    private final String text;

    private final String value;

    SupportLanguageEnum(String text, String value)
    {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues()
    {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SupportLanguageEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (SupportLanguageEnum anEnum : SupportLanguageEnum.values())
        {
            if (anEnum.value.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue()
    {
        return value;
    }

    public String getText()
    {
        return text;
    }
}
