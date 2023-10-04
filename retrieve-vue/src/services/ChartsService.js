import axios from "axios";

class ChartsService {
    static async getTotal() {
        const res = await axios.get(
            '/file/totalUploads',
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async getRecent() {
        const res = await axios.get(
            '/file/recentUploads',
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }
}

export default ChartsService