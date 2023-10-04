import axios from "axios";

class UserService {
    static async getUser(user) {
        const res = await axios.post(
            '/user/login',
            JSON.stringify(user)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async addUser(user) {
        const res = await axios.post(
            '/user/register',
            JSON.stringify(user)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }
}


export default UserService