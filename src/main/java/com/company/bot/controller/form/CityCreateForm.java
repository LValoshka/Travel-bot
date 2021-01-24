package com.company.bot.controller.form;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CityCreateForm {

    @NotNull
    private String name;

    @NotNull
    private String description;

}
