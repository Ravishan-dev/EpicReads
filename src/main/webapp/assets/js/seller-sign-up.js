async function createSellerAccount() {


    const name = document.getElementById("companyName");
    const email = document.getElementById("companyEmail");
    const mobile = document.getElementById("companyMobile");
    const password = document.getElementById("registerPassword");
    const confirmPassword = document.getElementById("confirmRegisterPassword");
    const agreed = document.getElementById("agreeTerms");

    const seller = {
        companyName: name.value,
        companyEmail: email.value,
        companyMobile: mobile.value,
        password: password.value,
        confirmPassword: confirmPassword.value,
        agreed: agreed.checked
    }

    try {
        const response = await fetch("api/sellers/create-account", {
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
                    },
                );
            } else {
                Notiflix.Notify.failure(data.message, {
                    'position': 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Account Creation Failed! Please Try Again", {
                'position': 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            'position': 'center-top'
        });
    }
}