package com.example.demo.DB;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Model.PessoaDto;

public class DB {

    public static List<PessoaDto> users = new ArrayList<>();

    static {
        users.add(new PessoaDto("thalisson", "123456", "admin"));
        users.add(new PessoaDto("willim", "123456", "user"));
    }
}
