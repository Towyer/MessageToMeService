package cn.edu.bjtu.weibo.service.serviceImpl;

import cn.edu.bjtu.weibo.service.MessageToMeService;
import cn.edu.bjtu.weibo.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageToMeService")
public class MessageToMeServiceImpl implements MessageToMeService {

	@Autowired
	UserDAO userDao;
	@Autowired
	WeiboDAO weiboDao;


	@Override
	public boolean atMeInfromWeibo(String userId, String weiboId) {

		userDao.insertWeiboAtMe(weiboDao.getOwner(weiboId), userId, weiboId);
		return true;
	}

	@Override
	public boolean atMeInfromComment(String userId, String commentId) {
		
		userDao.insertCommentAtMe(userId, commentId);
		return true;
	}

	@Override
	public boolean commentMyWeiboInform(String userId, String weiboId) {

		userDao.insertWeiboCommentMe(userId,weiboId);
		return true;
	}

	@Override
	public boolean commentMyCommentInform(String userId, String commentId) {

		userDao.insertCommentCommentMe(userId,commentId);
		return true;
	}

	@Override
	public boolean likeMyWeiboInform(String fromUserId, String toUserId, String weiboId) {

		userDao.insertWeiboLikeMe(fromUserId, toUserId, weiboId);
		return true;
	}

	@Override
	public boolean likeMyCommentInform(String fromUserId, String toUserId, String commentId) {
	
		userDao.insertCommentLikeMe(fromUserId, toUserId, commentId);
		return true;
	}
}
