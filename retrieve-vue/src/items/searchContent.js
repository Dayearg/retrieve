class SearchContent {
    constructor(type, participle, content, operation, precise, username,sortIndex,all) {
        this.type = type
        this.participle = participle
        this.content = content
        this.operation = operation
        this.precise = false
        this.username = username
        this.sortIndex=sortIndex
        this.all=all
    }
}

export default SearchContent