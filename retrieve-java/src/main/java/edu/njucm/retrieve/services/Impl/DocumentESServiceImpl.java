package edu.njucm.retrieve.services.Impl;

import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.model.DocumentES;
import edu.njucm.retrieve.services.DocumentESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentESServiceImpl implements DocumentESService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;

    @Value("${file.location}")
    String path;

    /**
     * 读文件
     *
     * @param fileName 文件夹名
     * @return
     */
    @Override
    public Document read(String fileName, String uploadUser) {

        List<DocumentES> list = new ArrayList<>();
        String location = path + uploadUser + "/" + fileName;
        File file = new File(location + "/res.txt");
        BufferedReader reader = null;

        int page = 0;
        int totalPages;
        int paragraph = 0;
        int source = 0;
        String title;
        String authors;
        String tags;
        String text;
        String imgName = "";
        Date published;
        Document document = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;

            tempString = reader.readLine();
            totalPages = Integer.parseInt(tempString);
            reader.readLine();
            title = reader.readLine();
            reader.readLine();
            authors = reader.readLine();
            reader.readLine();
            tempString = reader.readLine();
            if (!tempString.equals("null")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                published = new Date(format.parse(tempString).getTime());
            } else {
                published = null;
            }
            reader.readLine();
            tags = reader.readLine();
            java.util.Date date = new java.util.Date();//系统时间获取
            document = new Document(title, authors, published, tags, new Date(date.getTime()), uploadUser, fileName, totalPages);
            documentRepository.save(document);

            while (!(tempString = reader.readLine()).equals("0")) {

                if (tempString.length() != 0) {//读取的一行不为空
                    if (tempString.equals("-1")) {//读到-1，文本结束，改为图片
                        source = 1;
                        continue;
                    }

                    char[] strArr = tempString.toCharArray();//转为字符数组
                    if (strArr.length > 2 && ("" + strArr[0] + strArr[1]).equals("页码")) {//读到页码
                        String strNum = "";
                        for (int i = 2; i < strArr.length; i++) {
                            strNum = strNum + strArr[i];//获取页码数
                        }
                        page = Integer.parseInt(strNum);
                        paragraph = 0;//段落置0
                        continue;
                    }
                    paragraph++;
                    text = tempString;//获取文本

                    if (source > 0) {//若为图片
                        imgName = tempString;//获取图片名
                        tempString = reader.readLine();
                        if (!tempString.equals("null")) {
                            text = tempString;
                        } else {
                            text = "";
                        }
                    }

                    DocumentES documentES = new DocumentES(document, page, paragraph, source, text, imgName);
                    list.add(documentES);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        elasticsearchService.saveAll(list);
        return document;
    }

}
