//user entity

package com.ddia.ddia_labs.ch01;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //매개변수가 없는 기본 생성자(Default Constructor)를 만들어줘. 단, 외부에서는 못 쓰게(Protected) 막아줘.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK(기본키) 값은 내가 안 넣을게. DB 네가 알아서 1씩 증가시켜줘.
    private Long id;
    private String name;
    private String role;

    public User(String name, String role){
        this.name = name;
        this.role = role;
    }

}