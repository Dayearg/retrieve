import router from "@/routers/router";

class Jump {
    static async jumpToLogin() {
        await router.push(
            {
                path: "/login"
            }
        )
    }

    static async jumpToRegister() {
        await router.push(
            {
                path: "/register"
            }
        )
    }

    static async jumpToMain() {
        await router.push(
            {
                path: "/"
            }
        )
    }

    static async jumpToDocument() {
        await router.push(
            {
                path: "/document"
            }
        )
    }

    static async jumpToDetail(firstDocument, searchContent) {
        await router.push(
            {
                path: "/detail",
                query: {firstDocument: JSON.stringify(firstDocument), searchContent: JSON.stringify(searchContent)}
            }
        )
    }

    static async jumpToCollection() {
        await router.push(
            {
                path: "/collect"
            }
        )
    }

    static async jumpToBrowsing() {
        await router.push(
            {
                path: "/history"
            }
        )
    }

}

export default Jump