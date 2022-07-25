package com.swpu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swpu.reggie.common.R;
import com.swpu.reggie.domain.User;
import com.swpu.reggie.service.UserService;
import com.swpu.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
//            SMSUtils.sendMessage("");
//            session.setAttribute(phone,code);

            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码已发送");

        }

        return R.error("发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info(map.toString());

        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

//        Object codeInSession = session.getAttribute(phone);

        Object codeInSession = redisTemplate.opsForValue().get(phone);

        if (codeInSession != null && codeInSession.equals(code)){

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);

            if (user == null){

                user = new User();
                user.setPhone(phone);
                userService.save(user);

            }

            session.setAttribute("user",user.getId());

            redisTemplate.delete(phone);

            return R.success(user);
        }

        return R.error("登录失败");
    }


    /**
     * 退出功能
     *
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {

        request.getSession().removeAttribute("user");
        return R.success("登出成功");
    }


}
