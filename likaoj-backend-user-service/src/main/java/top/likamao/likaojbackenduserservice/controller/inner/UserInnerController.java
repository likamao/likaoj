package top.likamao.likaojbackenduserservice.controller.inner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.likamao.likaojbackendmodel.model.entity.User;
import top.likamao.likaojbackendserviceclient.service.UserFeignClient;
import top.likamao.likaojbackenduserservice.service.UserService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * note: 内部服务接口，用于服务间调用
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") Long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据用户ID集合获取用户信息列表
     *
     * @param userIdSet
     * @return
     */
    @Override
    @GetMapping("/list/ids")
    public List<User> listByIds(@RequestParam("isList") Collection<Long> isList) {
        return userService.listByIds(isList);
    }
}