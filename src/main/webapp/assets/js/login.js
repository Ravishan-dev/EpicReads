    async function login() {

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const email = document.getElementById("email");
    const password = document.getElementById("password");

    const user = {
        email: email.value,
        password: password.value
    }

    try {

        const response = await fetch("api/users/login", {
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
                    'EPIC READS',
                    data.message,
                    "Okay",
                    () => {
                        window.location = "index.html"
                    },
                );
            } else {
                Notiflix.Notify.warning(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Login Failed... Please Try Again", {
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