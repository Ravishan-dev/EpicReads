let params = new URLSearchParams(window.location.search);

const verificationContainer = document.getElementById("verificationCode");
const verificationInputs = verificationContainer.querySelectorAll(".code-input");
const userEmail = params.get("email");

function getVerificationCode() {
    return Array.from(verificationInputs).map(i => i.value.trim()).join("");
}

async function verifyAccount() {

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const verifyObj = {
        email: userEmail,
        verificationCode: getVerificationCode()
    }

    try {
        const response = await fetch("api/verify-accounts", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(verifyObj)
        });

        if (response.ok) {
            const data = await response.json();
            if (data.status) {
                Notiflix.Loading.remove();
                Notiflix.Report.success(
                    'EPIC READS',
                    data.message,
                    "Okay",
                    () => {
                        window.location = "login.html"
                    },
                );
            } else {
                Notiflix.Notify.warning(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Account Verification Failed", {
                'position': 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            'position': 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}