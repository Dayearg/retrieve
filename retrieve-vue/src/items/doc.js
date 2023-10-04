class doc {
    constructor(DocumentId, title, authors, uploadUser, published, tags, uploadTime, download, fileName, status,click) {
        this.DocumentId = DocumentId
        //文章标题
        this.title = title
        //作者
        this.authors = authors
        //发表时间
        this.published = published
        //标签
        this.tags = tags
        //上传时间
        this.uploadTime = uploadTime
        //上传用户
        this.uploadUser = uploadUser
        //下载次数
        this.download = download
        //状态
        this.status = status
        //文件夹名
        this.fileName = fileName
        //编辑
        this.editable = false
        //点击次数
        this.click=click
    }
}

export default doc