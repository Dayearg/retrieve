import axios from "axios";

class SearchService {
    static async firstSearch(searchContent) {
        const res = await axios.post(
            "/search/first",
            JSON.stringify(searchContent)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async secondSearch(search) {
        const res = await axios.post(
            "/search/second",
            JSON.stringify(search)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }
}

export default SearchService