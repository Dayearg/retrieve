package edu.njucm.retrieve.services.Impl;


import edu.njucm.retrieve.dao.CollectRepository;
import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.dao.ESRepository;
import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.model.DocumentFirstSearch;
import edu.njucm.retrieve.model.SearchContent;
import edu.njucm.retrieve.services.DocumentESService;
import edu.njucm.retrieve.services.DocumentService;
import edu.njucm.retrieve.services.RPCService;
import edu.njucm.retrieve.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ESRepository esRepository;

    @Autowired
    private DocumentESService documentESService;

    @Autowired
    private RPCService rpcService;

    @Autowired
    private CollectRepository collectRepository;


    @Value("${file.location}")
    String path;

    @Value("${file.ip}")
    String ip;

    /**
     * 文件上传功能实现
     *
     * @param file
     * @param username
     * @return
     */

    @Override
    public Document add(MultipartFile file, String username) {

        if (Objects.isNull(file) || file.isEmpty()) {
            return null;//文件为空
        }

        String location = path + username + "/temp/" + file.getOriginalFilename();//文件存储路径

        //查看路径所在的文件是否存在
        File folder = new File(location);
        if (!folder.isDirectory()) {
            folder.mkdirs();//目录不存在则创建
        }
        try {// 文件保存
            file.transferTo(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String folderName = rpcService.sendAndReceive(location);//获取文件夹名
        if (folderName.length() != 0) {
            return documentESService.read(folderName, username);
        }
        return null;
    }

    /**
     * 文件下载功能实现
     *
     * @param documents
     * @return
     */
    @Override
    public String[] download(Document[] documents) {
        for (int i = 0; i < documents.length; i++) {
            Document document = documentRepository.findByTitleAndUploadUser(documents[i].getTitle(), documents[i].getUploadUser());
            document.setDownload(document.getDownload() + 1);
            documentRepository.save(document);
            documents[i] = document;
        }
        String[] urls = new String[documents.length];
        if (documents.length < 1) {
            urls = null;
        } else if (documents.length < 4) {
            for (int i = 0; i < documents.length; i++) {
                urls[i] = ip + documents[i].getUploadUser() + "/" + documents[i].getFileName() + "/" + documents[i].getFileName() + ".pdf";
            }
        } else {
            String location = documents[0].getUploadUser() + "/temp";
            //查看路径所在的文件是否存在
            File folder = new File(path + location);
            if (!folder.isDirectory()) {
                folder.mkdirs();//目录不存在则创建
            }
            location += "/文献.zip";//压缩文件名
            folder = new File(location);
            if (folder.exists()) {
                folder.delete();
            }
            try {
                //ZipOutputStream类：完成文件或文件夹的压缩
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path + location));
                for (Document document : documents) {
                    File file = new File(document.getFileName() + "/" + document.getFileName() + ".pdf");
                    InputStream input = new FileInputStream(file);
                    // 给列表中的文件单独命名
                    zipOut.putNextEntry(new ZipEntry(document.getTitle()));
                    int temp;
                    //读取相关的文件
                    while ((temp = input.read()) != -1) {
                        //写入输出流中
                        zipOut.write(temp);
                    }
                    //关闭流
                    zipOut.closeEntry();
                    input.close();
                }
                zipOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            urls[0] = ip + location;
        }
        return urls;
    }

    /**
     * 文件删除功能实现
     *
     * @param documents
     * @return
     */
    @Override
    public boolean delete(Document[] documents) {
        boolean state = false;
        for (Document document : documents) {
            String location = path + document.getUploadUser() + "/" + document.getFileName();
            state = FileSystemUtils.deleteRecursively(new File(location));
            document = documentRepository.findByTitleAndUploadUser(document.getTitle(), document.getUploadUser());
            documentRepository.delete(document);
            esRepository.deleteByTitleAndUploadUser(document.getTitle(), document.getUploadUser());
        }
        return state;
    }

    @Override
    public List<Document> refresh(String username) {
        return documentRepository.findAllByUploadUser(username);
    }

    @Override
    public Document revise(Document document) {
        Long id = documentRepository.findByTitleAndUploadUser(document.getTitle(), document.getUploadUser()).getDocumentId();
        document.setDocumentId(id);
        documentRepository.save(document);
        return documentRepository.findByDocumentId(id);
    }

    @Override
    public String preview(Document document) {
        Document doc = documentRepository.findByTitleAndUploadUser(document.getTitle(), document.getUploadUser());
        String url = ip + doc.getUploadUser() + "/" + doc.getFileName() + "/" + doc.getFileName() + ".pdf";
        return url;
    }

    @Override
    public Map<String, Long> recentUploads() {
        Map<String, Long> map = new LinkedHashMap<>();
        List<Map<String, Object>> list = documentRepository.findRecentUploads();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (i < 5) {
                Date date = (Date) list.get(i).get("0");
                Long number = (Long) list.get(i).get("1");
                SimpleDateFormat format = new SimpleDateFormat("MM-dd");
                String str = format.format(date);
                map.put(str, number);
            }
        }
        return map;
    }

    @Override
    public Map<String, Long> totalUploads() {
        Map<String, Long> map = new LinkedHashMap<>();
        List<Map<String, Object>> list = documentRepository.findTotalUploads();
        Long sum = 0L;
        for (int i = 0; i < list.size(); i++) {
            Date date = (Date) list.get(i).get("0");
            Long number = (Long) list.get(i).get("1");
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String str = format.format(date);
            sum = sum + number;
            map.put(str, sum);
        }
        return map;
    }

    @Override
    public List<Document> search(SearchContent searchContent) {
        if (Objects.equals(searchContent.getType(), "default")) {
            return documentRepository.findByTitleLike("%" + searchContent.getContent() + "%");
        }else if (Objects.equals(searchContent.getType(), "title")) {
            return documentRepository.findByTitleLike("%" + searchContent.getContent() + "%");
        }
        else if (Objects.equals(searchContent.getType(), "authors")){
            return documentRepository.findByAuthorsLike( "%"+searchContent.getContent() + "%");
        } else if (Objects.equals(searchContent.getType(), "tags")){
            return documentRepository.findByTagsLike("%" + searchContent.getContent() + "%");
        }else {
            return null;
        }
    }

}
