package cn.iocoder.educate.module.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 这里肯定是对实体类的操作，而不是对bo的操作，所以需要大篇幅的代码去写重复的逻辑
 *
 * @author j-sentinel
 * @date 2024/4/13 18:09
 */
public interface BaseService {

    /**
     * 这里的主要优化就是无需再去new对象
     * @param user
     * @param userBoClass
     * @param <R>
     * @param <M>
     * @return
     */
    default <R,M> R tranferBo(M user,Class<R> userBoClass){
        try {
            // 反射实例化
            R userBo = userBoClass.newInstance();
            BeanUtils.copyProperties(user,userBo);
            return userBo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 可以把List转换为对象(通过copyProperties的方法)
     * @param userList Category(id=1, title=Java, description=Java111111222, createTime=Wed Apr 27 21:25:38 CST 2022, updateTime=Sat Sep 03 19:39:50 CST 2022, status=1, sorted=1, isdelete=0, pid=0)
     * @param cla
     * @param <R>
     * @param <M>
     * @return
     */
    default <R,M> List<R> tranferListBo(List<M> userList, Class<R> cla){
        return userList.stream().map(user -> {
            return tranferBo(user,cla);
        }).collect(Collectors.toList());
    }

    /**
     * 将给定源bean的属性值复制到目标bean中
     * @param userPage Course(id=4, title=Springboot + vue 前后端分离后台管理系统 Demo, content=null, tags=typescript,javascript,html,css, description=完成一个照片分享应用的全部界面，包含界面布局、主题还有各种复杂的界面。不依赖第三方组件库，亲手完成所有组件。, categoryid=1, gotop=0, views=1030, createTime=Tue Sep 06 10:44:01 CST 2022, updateTime=Tue Sep 06 15:21:51 CST 2022, comment=1, isdelete=0, htmlcontent=null, categorytitle=Java, userid=1, vip=0, avatar=https://p6-passport.byteacctimg.com/img/user-avatar/a99401d07f0e29e915a74017b9d44d85~300x300.image, nickname=飞哥, status=1, img=https://itbooking.oss-cn-guangzhou.aliyuncs.com/note/2022/04/15/5cacb93d-db76-415a-b54f-a0f0afc24eab.jpg, collects=0, comments=1006, coursetimer=10时20分, price=1003, realprice=1029, coursetype=3, sorted=1, beginer=难, isnew=1, ishot=1, ispush=1)
     * @param cla
     * @param <R>
     * @param <M>
     * @return
     */
    default <R,M> Page<R> tranferPageBo(IPage<M> userPage, Class<R> cla){
        List<R> userBoList = tranferListBo(userPage.getRecords(),cla);
        Page<R> userBoPage = new Page<>();
        userBoPage.setRecords(userBoList);
        userBoPage.setTotal(userPage.getTotal());
        userBoPage.setSize(userPage.getSize());
        userBoPage.setPages(userPage.getPages());
        userBoPage.setCurrent(userPage.getCurrent());
        return userBoPage;
    }

    /**
     * 第二种方式
     * @param clz 获取clz中全部的属性，并转换为数据库中格式的列数据
     * @param filterField 剔除的属性
     * @param <T>
     * @return
     */
    default <T> String[] getFilterFields(Class<T> clz, String ...filterField) {
        List<String> list = new ArrayList<>();
        List<String> result = new ArrayList<>();
        Field[] fields = clz.getDeclaredFields();
        for(Field field : fields) {
            list.add(Tool.humpToLine(field.getName()));
        }
        if(filterField!=null && filterField.length > 0) {
            result = list.stream().filter(field -> Arrays.asList(filterField).stream()
                    .filter(f -> f.equals(field)).count() == 0).collect(Collectors.toList());
        }else{
            result =  list;
        }
        if(!CollectionUtils.isEmpty(result)) {
            String[] strings = new String[result.size()];
            for (int i = 0; i < result.size(); i++) {
                strings[i] = result.get(i);
            }
            return strings;
        }
        return null;
    }

    /**
     * 第一种方式
     * @param clz 获取clz中全部的属性，并转换为数据库中格式的列数据
     * @param filterField 剔除的属性
     * @param <T>
     * @return
     */
    @Deprecated
    default <T> List<String> getFilterFields(Class<T> clz, List<String> filterField) {
        List<String> list = new ArrayList<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            list.add(Tool.humpToLine(field.getName()));
        }
        if(!CollectionUtils.isEmpty(filterField)){
            List<String> collect = list.stream().filter(field -> filterField.stream()
                    .filter(f -> f.equals(field)).count() == 0).collect(Collectors.toList());
            return collect;
        }else{
            return list;
        }
    }

}
