const {createApp} = Vue 

const app = createApp({
    data() {
        return {
            typeLoan: [],
            paymentsLoan: [],
            paymentsLoanArray: [],
            account: undefined,
            accountsNumber: [],
            typeLoanRadio: '',
            errorMsg: undefined,
            paymentLoanRadio: undefined,
            quoteAmount: undefined,
            amount: undefined,
            maxAmount: undefined,
            idLoan: undefined
        }
    },
    created() {
        this.loanData()
        this.selectAccounts()
    },
    methods: {
        loanData() {
            axios.get('/api/loans')
            .then(response => {
                this.typeLoan = response.data
            })
        },
        selectAccounts() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accounts = response.data
                    this.accountsNumber = response.data.map(prop => prop.number)
                })
                .catch(error => console.error(error))
        },
        makeLoan() {
            this.idLoan = this.typeLoan.find(loan => loan.id === this.typeLoanRadio).id
            axios.post('/api/loans', {id: this.typeLoanRadio, amount: this.amount, payment: this.paymentLoanRadio, numberDestiny: this.account})
                .then(() => {
                    Swal.fire(
                        'Successful transfer',
                        '',
                        'success'
                    )
                    setTimeout(() => location.href = '/web/loan-application.html', 2000)
                })
                .catch(error => {
                    this.errorMsg = error.response.data
                    if(this.amount < 500) {
                        Swal.fire( {
                            icon: 'error',
                            title: 'Oops...',
                            text: this.errorMsg
                        })
                    } else if(this.typeLoanRadio == this.idLoan) {
                        Swal.fire( {
                            icon: 'error',
                            title: 'Data incomplete',
                            text: this.errorMsg
                        })
                    } else if(this.amount > this.maxAmount) {
                        Swal.fire( {
                            icon: 'error',
                            title: 'Exceeds the amount',
                            text: this.errorMsg
                        })
                    } else if(this.amount === 0 || this.paymentLoanRadio === 0 || this.account == '') {
                        Swal.fire( {
                            icon: 'question',
                            title: 'Data incomplete',
                            text: this.errorMsg
                        })
                    } else {
                        Swal.fire( {
                            icon: 'question',
                            title: 'Data incomplete',
                            text: this.errorMsg
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
        filterPayments() {
            this.paymentsLoan = this.typeLoan.find(prop => prop.id === this.typeLoanRadio)
            if(this.paymentsLoan) {
                this.paymentsLoanArray = this.paymentsLoan.payments
                this.maxAmount = this.paymentsLoan.maxAmount
            }
        },
        quoteAmounts() {
            this.quoteAmount = this.amount * 1.20 / this.paymentLoanRadio
        }
    }
})

app.mount('#content')