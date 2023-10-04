const newPage = (url) => {
    const link = document.createElement('a');
    link.href = `http://${url}`;
    link.target = '_blank';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

export default newPage