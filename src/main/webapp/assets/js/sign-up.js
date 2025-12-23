async function signup() {

    const fname = document.getElementById("first");
    const lname = document.getElementById("last");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const cpassword = document.getElementById("confirm");

    const user = {
        firstName: fname.value,
        lastName: lname.value,
        email: email.value,
        password: password.value,
        confirmPassword: cpassword.value
    }

    try {
        const response = await fetch("api/users", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {
            const data = await response.json();
            console.log(data);
            console.log(data.message);
        } else {

        }

    } catch (e) {
        console.log("exception");
    }
}