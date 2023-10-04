package edu.njucm.retrieve.controller;

import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.model.DocumentFirstSearch;
import edu.njucm.retrieve.model.SearchContent;
import edu.njucm.retrieve.services.CollectService;
import edu.njucm.retrieve.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/file")
@CrossOrigin
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CollectService collectService;

    /**
     * 文件上传功能
     */
    @PostMapping("/upload")
    public @ResponseBody Document add(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        return documentService.add(file, username);
    }

    /**
     * 文件下载功能
     *
     * @param documents Document数组
     * @return String数据 nginx直链
     */
    @PostMapping("/download")
    public @ResponseBody String[] download(@RequestBody Document[] documents) {
        return documentService.download(documents);
    }

    /**
     * 文件删除功能
     *
     * @param documents Document数组
     * @return 成功与否
     */
    @PostMapping("/delete")
    public @ResponseBody boolean delete(@RequestBody Document[] documents) {
        return documentService.delete(documents);
    }

    /**
     * 刷新
     *
     * @param username
     * @return
     */
    @GetMapping("/refresh/{username}")
    public @ResponseBody List<Document> refresh(@PathVariable("username") String username) {
        return documentService.refresh(username);
    }

    /**
     * 更新
     *
     * @param document
     * @return
     */
    @PostMapping("/revise")
    public @ResponseBody Document revise(@RequestBody Document document) {
        return documentService.revise(document);
    }

    /**
     * 预览
     *
     * @param document
     * @return
     */
    @PostMapping("/preview")
    public @ResponseBody String preview(@RequestBody Document document) {
        return documentService.preview(document);
    }

    @GetMapping("/recentUploads")
    public @ResponseBody Map<String, Long> recentUploads() {
        return documentService.recentUploads();
    }

    @GetMapping("/totalUploads")
    public @ResponseBody Map<String, Long> totalUploads() {
        return documentService.totalUploads();
    }

    @PostMapping("/collect")
    public @ResponseBody int collect(@RequestBody Map<String, Object> object) {
        return collectService.collect((String) object.get("username"), (String) object.get("uploadUser"), (String) object.get("title"));
    }

    /**
     * 加载收藏文件
     *
     * @param username
     * @return
     */
    @GetMapping("/loadCollect/{username}")
    public @ResponseBody List<Document> loadCollect(@PathVariable("username") String username) {
        return collectService.load(username);
    }

    @PostMapping("/remote")
    public @ResponseBody List<Document> remoteSearch(@RequestBody SearchContent searchContent) {
        return documentService.search(searchContent);
    }
}
