fetch('https://jsonplaceholder.typicode.com/users')
    .then(res => {
        return res.json();
    })
    .then(data => {
        const maxUsers = 100;
        const result = `Players: ${data.length}/${maxUsers}`;
        document.querySelector('ella').textContent = result;
    })
    .catch(error => console.log(error));