package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.Notice;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知公告 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/notice")
public class NoticeController extends BaseController {

    @Autowired
    private INoticeService noticeService;

    /**
     * 查询${tableComment}
     */
    @HasPermissions("system:notice:query")
    @GetMapping("get/{noticeId}")
    public Notice get(@PathVariable("noticeId") Long noticeId) {
        return noticeService.selectNoticeById(noticeId);

    }

    /**
     * 查询通知公告列表
     */
    @HasPermissions("system:notice:list")
    @GetMapping("list")
    public R list(Notice notice) {
        startPage();
        return result(noticeService.selectNoticeList(notice));
    }


    /**
     * 新增保存通知公告
     */
    @HasPermissions("system:notice:add")
    @OperLog(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("save")
    public R addSave(@RequestBody Notice notice) {
        notice.setParkId(getParkId());
        notice.setCreateBy(getLoginName());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改保存通知公告
     */
    @HasPermissions("system:notice:edit")
    @OperLog(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R editSave(@RequestBody Notice notice) {
        notice.setUpdateBy(getLoginName());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除${tableComment}
     */
    @HasPermissions("system:notice:remove")
    @OperLog(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(noticeService.deleteNoticeByIds(ids));
    }

}
