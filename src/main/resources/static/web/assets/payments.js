const { createApp } = Vue

const app = createApp({
    data() {
        return {
            cardHolderSelect: '',
            cardNumber: '',
            expDate: '',
            cvv: undefined,
            amount: undefined,
            accountSelected: undefined,
            description: '',
            accounts: [],
            accountCurrent: [],
            cardCurrent: [],
            accountDestiny: '',
            balanceAccount: Number
        }
    },
    created() {
        this.loadAccounts()
    },
    methods: {
        loadAccounts() {
            axios.get('/api/clients/current')
                .then(response => this.accounts = response.data.accounts)
                .catch(error => console.error(error))
        },
        makePay() {
            axios.post('/api/clients/current/payment', {
                cardHolder: this.cardHolderSelect,
                number: this.cardNumber,
                thruDate: this.expDate,
                cvv: this.cvv,
                amount: this.amount,
                description: this.description,
                accountDestiny: this.accountDestiny
            })
                .then(() => {
                    Swal.fire(
                        'Payment made!',
                        'The amount of your account was deducted',
                        'success'
                    )
                        .then(() => window.location.reload())
                })
                .catch(() => {
                    if (this.cardHolderSelect == undefined || this.cardNumber == undefined || this.expDate === undefined || this.cvv === undefined || this.amount === undefined || this.description === undefined || this.accountDestiny === undefined) {
                        Swal.fire({
                            icon: 'question',
                            title: 'Data incomplete',
                            text: 'Please, check that all fields are completed',
                        })
                    } else if (this.cardHolderSelect == undefined || this.cardHolderSelect == '') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Write the cardHolder',
                        })
                    } else if (this.cardNumber == undefined || this.cardNumber == '') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Write the Number card',
                        })
                    } else if (this.expDate === undefined) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Select the expiration date',
                        })
                    } else if (this.amount == 0 || this.amount == undefined) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'The amount is 0',
                        })
                    } else if (this.amount > this.balanceAccount) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'The amount of the account was exceeded',
                        })
                    } else if (this.cvv == 0 || this.cvv == undefined) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "The cvv can't be 0",
                        })
                    } else if (this.description == undefined || this.description == '') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "Write a description",
                        })
                    } else if (this.accountSelected == this.accountDestiny) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "The account destiny is equal to account origin",
                        })
                    } else if (this.accountDestiny == undefined || this.accountDestiny == '') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "Write an account destiny",
                        })
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "Error data",
                        })
                    }
                })
        },
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html')
        }
    },
    computed: {
        filterByAccount() {
            this.accountCurrent = this.accounts.find(prop => prop.number === this.accountSelected)
            if (this.accountCurrent) {
                this.balanceAccount = this.accountCurrent.balance
                this.cardCurrent = this.accountCurrent.cards
            }
        }
    }
})
app.mount('#content')