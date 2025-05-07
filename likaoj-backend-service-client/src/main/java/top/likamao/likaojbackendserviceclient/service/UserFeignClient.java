package top.likamao.likaojbackendserviceclient.service;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.likamao.likaojbackendcommon.common.ErrorCode;
import top.likamao.likaojbackendcommon.exception.BusinessException;
import top.likamao.likaojbackendmodel.model.entity.User;
import top.likamao.likaojbackendmodel.model.enums.UserRoleEnum;
import top.likamao.likaojbackendmodel.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static top.likamao.likaojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务-共享接口
 *
 * @author <a href="https://github.com/likaboy">LIKA</a>
 */
@FeignClient(value = "likaoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") Long userId);

    /**
     * 根据用户ID集合获取用户信息列表
     *
     * @param userIdSet
     * @return
     */
    @GetMapping("/list/ids")
    List<User> listByIds(@RequestParam("isList") Collection<Long> isList);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
