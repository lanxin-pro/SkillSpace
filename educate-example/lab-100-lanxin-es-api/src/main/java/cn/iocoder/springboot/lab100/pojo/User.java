package cn.iocoder.springboot.lab100.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author j-sentinel
 * @date 2024/2/16 10:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {

    private String name;

    private int age;

}
