import axios from "axios";
import globalVar from "@/model/globalVar";

class HistoryService {
    static async getSearchHistory(searchItem) {
        const res = await axios.post(
            '/history/search/get',
            JSON.stringify(searchItem)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }


    static async getBrowsingHistory(username) {
        const res = await axios.post(
            '/history/browsing/get',
            {"username":username,"date":globalVar.Calendar.value}
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async getRemoteSearch(content) {
        const res = await axios.post(
            '/file/remote',
            JSON.stringify(content)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

}

export default HistoryService