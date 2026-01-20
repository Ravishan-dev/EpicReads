window.addBook = async function () {
    const title = document.getElementById("bookTitle");
    const isbn = document.getElementById("bookIsbn");
    const description = document.getElementById("bookDescription");
    const author = document.getElementById("bookAuthor");
    const price = document.getElementById("bookPrice");
    const qty = document.getElementById("bookQty");
    const categoryId = document.getElementById("bookCategory");

    const category = parseInt(categoryId.value);
    if (categoryId === 0 || isNaN(categoryId.value)) {
        Notiflix.Notify.failure("Please Select A Category", {
            'position': 'center-top'
        });
        return;
    }

    const book = {
        tittle: title.value,
        isbn: isbn.value,
        description: description.value,
        author: author.value,
        price: price.value,
        qty: qty.value,
        category: category
    }

    try {
        const response = await fetch("api/books/add-books", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(book)
        });

        if (response.ok) {
            const data = await response.json();
            if (data.status) {
                Notiflix.Report.success(
                    'EPIC READS',
                    data.message,
                    "Okay",
                    () => {
                        window.location = 'index.html'
                    }
                );
            } else {
                Notiflix.Notify.failure(data.message, {
                    'position': 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Item Insertion Failed.. Please Try Again", {
                'position': 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            'position': 'center-top'
        });
    }
}