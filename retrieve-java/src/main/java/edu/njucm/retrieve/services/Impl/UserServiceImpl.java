package edu.njucm.retrieve.services.Impl;

import edu.njucm.retrieve.dao.UserRepository;
import edu.njucm.retrieve.model.User;
import edu.njucm.retrieve.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * 登录判断
     *
     * @param user
     * @return
     */
    public int login(User user) {
        User res = userRepository.findByName(user.getName());
        //判断用户名是否存在
        if (res == null) {
            return -1;
        } else {
            //判断密码是否正确
            if (!res.getPasswd().equals(user.getPasswd())) {
                return -2;
            }
        }
        return 0;
    }

    /**
     * 注册判断
     *
     * @param user
     * @return
     */
    public int register(User user) {
        User res = userRepository.findByName(user.getName());
        if (res == null) {
            //保存到数据库
            userRepository.save(user);
            return 0;
        } else {//注册失败，用户名已存在
            return -1;
        }
    }


}
