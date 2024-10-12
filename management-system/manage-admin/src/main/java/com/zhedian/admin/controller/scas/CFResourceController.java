package com.zhedian.admin.controller.scas;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.scas.entity.ScasCourse;
import com.zhedian.provide.scas.entity.ScasCourseResource;
import com.zhedian.provide.scas.entity.ScasMyCourses;
import com.zhedian.provide.scas.service.IScasCourseResourceService;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.scas.service.IScasMyCoursesService;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 协同过滤推荐算法
 */
@Api(tags = "推荐学习资源")
@RestController
@RequestMapping("/scas/cf")
public class CFResourceController {

    @Resource
    private IScasCourseResourceService courseResourceService;

    @Resource
    private IScasMyCoursesService myCoursesService;

    @Resource
    private SysUserService userService;

    @Resource
    private IScasCourseService courseService;



    @ApiOperation("获取推荐学习资源")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<List<ScasCourseResource>> getRecommendResource() {
        // 获取当前用户
        SysUser user = AuthService.getUser();
        Integer targetUserId = user.getId();
        // 获取推荐学习资源
        List<ScasCourseResource> courseResourceList = getRecommendJobs(targetUserId);
        return CommonResult.success(courseResourceList);
    }


    /**
     * 获取推荐学习资源-基于用户报名的协同过滤推荐算法
     *
     * @param targetUserId 目标用户id
     * @return 推荐学习资源
     */
    private List<ScasCourseResource> getRecommendJobs(int targetUserId) {
        List<ScasCourseResource> courseResourceList;
        //UserId为空不进行计算
        if (targetUserId == 0) {
            courseResourceList = getCourseResourceList();
        } else {
            // 获取用户已报名的课程
            Map<Integer, Set<Integer>> userNewsMap = getUserCollectNews();
            // 获取相似用户
            List<Integer> similarUsers = getSimilarUsers(targetUserId, userNewsMap);
            // 获取相似用户报名的课程id
            Set<Integer> similarUserNews = getSimilarUserNews(targetUserId, similarUsers, userNewsMap);
            // 获取推荐学习资源
            if (similarUserNews.size() > 0) {
                courseResourceList = courseResourceService.list(new QueryWrapper<ScasCourseResource>().in("course_id", similarUserNews));
            } else {
                courseResourceList = getCourseResourceList();
            }
        }
        // 查询课程名称
        if (ObjectUtils.isNotEmpty(courseResourceList)) {
            for (ScasCourseResource resource : courseResourceList) {
                ScasCourse course = courseService.getById(resource.getCourseId());
                if (ObjectUtils.isNotNull(course)) {
                    resource.setCourseName(course.getCourseName());
                }
            }
        }
        return courseResourceList;
    }

    /**
     * 随机获取100条学习资源
     *
     * @return
     */
    private List<ScasCourseResource> getCourseResourceList() {
        List<ScasCourseResource> courseResourceList;
        List<ScasCourseResource> list = courseResourceService.list();
        // 将list随机排序
        Collections.shuffle(list);
        // 取前100条
        courseResourceList = list.stream().limit(100).collect(Collectors.toList());
        return courseResourceList;
    }


    /**
     * 获取用户报名的课程
     *
     * @return 用户报名的课程 Map<userId, Set<courseId>> 用户id，用户报名的课程id
     */
    private Map<Integer, Set<Integer>> getUserCollectNews() {
        Map<Integer, Set<Integer>> userNewsMap = new HashMap<>();
        //获取所有用户
        List<SysUser> users = userService.list();
        for (SysUser user : users) {
            //获取用户报名的课程
            LambdaQueryWrapper<ScasMyCourses> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ScasMyCourses::getUserName, user.getUserName());
            List<ScasMyCourses> myCoursesList = myCoursesService.list(wrapper);
            //取出jobId
            Set<Integer> collect = myCoursesList.stream().map(ScasMyCourses::getCourseId).collect(Collectors.toSet());
            //存储用户收藏的岗位id
            userNewsMap.put(user.getId(), collect);
        }
        return userNewsMap;
    }

    /**
     * 获取相似用户
     *
     * @param targetUserId 目标用户id
     * @param userNewsMap  用户报名的课程
     * @return 相似用户id列表
     */
    private List<Integer> getSimilarUsers(int targetUserId, Map<Integer, Set<Integer>> userNewsMap) {
        List<Integer> similarUsers = new ArrayList<>();
        // 计算目标用户与其他用户的相似度
        for (Map.Entry<Integer, Set<Integer>> entry : userNewsMap.entrySet()) {
            int userId = entry.getKey();
            if (userId != targetUserId) {
                double similarity = cosineSimilarity(userNewsMap.get(targetUserId), entry.getValue());
                // 可以根据具体情况设定相似度阈值
                if (similarity > 0.5) {
                    similarUsers.add(userId);
                }
            }
        }
        return similarUsers;
    }

    /**
     * 计算余弦相似度
     *
     * @param set1 目标用户报名的课程
     * @param set2 相似用户报名的课程
     * @return
     */
    private double cosineSimilarity(Set<Integer> set1, Set<Integer> set2) {
        // 交集
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // 计算余弦相似度
        double magnitude1 = Math.sqrt(set1.size());
        double magnitude2 = Math.sqrt(set2.size());

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0; // 避免除零错误
        }

        return intersection.size() / (magnitude1 * magnitude2);
    }

    /**
     * 相似用户报名的课程id
     *
     * @param targetUserId 目标用户id
     * @param similarUsers 相似用户
     * @param userNewsMap  用户报名的课程
     * @return 相似用户报名的课程id
     */
    private Set<Integer> getSimilarUserNews(int targetUserId, List<Integer> similarUsers, Map<Integer, Set<Integer>> userNewsMap) {
        Set<Integer> neighboringNews = new HashSet<>();

        // 获取用户报名的课程id
        for (int neighborUserId : similarUsers) {
            neighboringNews.addAll(userNewsMap.get(neighborUserId));
        }

        // 移除目标用户已报名的课程id
        neighboringNews.removeAll(userNewsMap.get(targetUserId));

        return neighboringNews;
    }
}
