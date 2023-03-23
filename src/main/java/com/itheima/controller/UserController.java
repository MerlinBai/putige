package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.entity.User;
import com.itheima.service.UserService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/18 19 : 03
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //生成随机验证码
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务api完成信息发送
            SMSUtils.sendMessage("瑞吉外卖","SMS_274535132",phone,code);
            //把验证码保存到session种
           // session.setAttribute(phone,code);

            //将生成的验证码缓存到redis中，并且设置有效期为5分钟

            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);


           session.setAttribute("user",user.getId());
           return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){

//获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);

        //从redis中获取缓存的验证码对象
        Object codeInSession =redisTemplate.opsForValue().get(phone);

        //进行验证码比对
        if(codeInSession!=null && codeInSession.equals(code)){
    //如果能比对成功 登陆成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if (user==null){
               user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果登陆成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success(user);

        }
        return R.error("信息发送失败");
    }
}
