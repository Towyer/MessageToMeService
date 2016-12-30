package cn.edu.bjtu.weibo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.LoginDAO;
import cn.edu.bjtu.weibo.model.LoginUser;
import cn.edu.bjtu.weibo.service.RegisterService;
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	LoginDAO loginDao;
	@Override
	public boolean registerNewUser(LoginUser user) {
		//插入数据库，生成新用户
		if(loginDao.insert(user.getUsername(), user.getPassword()))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isUserNameExisted(String username) {
		return loginDao.select(username);
	}

}
