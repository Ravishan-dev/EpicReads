async function signup() {

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

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
            if (data.status) {
                Notiflix.Report.success(
                    'Epic Reads',
                    data.message,
                    "Okay",
                    () => {
                        window.location = "login.html";
                    },
                );
            } else {
                Notiflix.Notify.warning(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Nofity.failure("Something Went Wrong Please Try Again", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}