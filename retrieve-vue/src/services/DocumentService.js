import axios from "axios";


class DocumentService {

    static async download(documents) {
        const res = await axios.post(
            '/file/download',
            JSON.stringify(documents)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async delete(documents) {
        const res = await axios.post(
            '/file/delete',
            JSON.stringify(documents)
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async refresh(username) {
        const res = await axios.get(
            `file/refresh/${username}`,
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }

    static async preview(row) {
        const res = await axios.post(
            'file/preview',
            JSON.stringify(row)
        )
        try {
            return res.data
        } catch
            (error) {
            console.log(error)
        }
    }

    static async download(row) {
        const res = await axios.post(
            'file/download',
            JSON.stringify(row)
        )
        try {
            for (let i = 0; i < res.data.length; i++) {
                const xhr = new XMLHttpRequest();
                xhr.open('GET', `http://${res.data[i]}`, true);
                xhr.responseType = 'blob';
                xhr.onload = function () {
                    if (this.status === 200) {
                        const blob = new Blob([this.response], {type: 'application/pdf'});
                        const link = document.createElement('a');
                        link.href = URL.createObjectURL(blob);
                        link.download = res.data[i].substring(res.data[i].lastIndexOf('/') + 1);
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                        URL.revokeObjectURL(link.href);
                    }
                };
                xhr.send();
            }
        } catch
            (error) {
            console.log(error)
        }
    }

    static async revise(row) {
        const res = await axios.post(
            'file/revise',
            JSON.stringify(row)
        )
        try {
            return res.data
        } catch
            (error) {
            console.log(error)
        }
    }

    static async collect(row) {
        console.log(JSON.stringify(row))
        const res = await axios.post(
            'file/collect',
            JSON.stringify(row)
        )
        try {
            return res.data
        } catch
            (error) {
            console.log(error)
        }
    }

    static async loadCollection(username) {
        const res = await axios.get(
            `file/loadCollect/${username}`,
        )
        try {
            return res.data
        } catch (err) {
            return err
        }
    }
}


export default DocumentService
