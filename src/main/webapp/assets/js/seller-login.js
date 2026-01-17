async function sellerLogin() {
    const email = document.getElementById("loginEmail");
    const password = document.getElementById("loginPassword");
    const rememberMe = document.getElementById("rememberMe");

    const seller = {
        companyEmail: email.value,
        password: password.value,
        rememberMe: rememberMe.checked
    }

    try {
        const response = await fetch("api/sellers/login", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(seller)
        });

        if (response.ok) {
            const data = await response.json();
            if (data.status) {
                Notiflix.Report.success(
                    'EPIC READS',
                    data.message,
                    'Okay',
                    () => {
                        window.location = "index.html"
                    }
                );
            } else {
                Notiflix.Notify.failure(data.message, {
                    'position': 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Login Failed", {
                'position': 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            'position': 'center-top'
        });
    }
}