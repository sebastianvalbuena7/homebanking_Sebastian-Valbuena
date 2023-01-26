const {createApp} = Vue

const app = createApp({
    data() {
        return {
            typeAccount: undefined,
            accountOwner: undefined,
            accountOther: undefined,
            money: undefined,
            description: undefined,
            accountOrigin: undefined,
            accounts: [],
            balanceAccounts: [],
            excludeAccounts: [],
            accountsNumber: [],
            message: ''
        }
    },
    created() {
        this.selectAccounts()
    },
    methods: {
        makeTransaction() {
            axios.post('/api/transactions', `amount=${this.money}&description=${this.description}&numberOrigin=${this.accountOrigin}&numberDestiny=${this.accountOwner || this.accountOther}`)
                .then(() => {
                    Swal.fire(
                        'Successful transfer',
                        '',
                        'success'
                    )
                    setTimeout(() => location.href = '/web/transfer.html', 2000)
                })
                .catch(response => {
                    this.message = response.response.data
                    if(this.message == 'Cuentas iguales') {
                        Swal.fire( {
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Equal accounts'
                        })
                    } else if(this.message == 'La cuenta no tiene monto suficiente') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'The account does not have enough amount',
                        })
                    } else if(this.message == 'La cuenta no existe') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'The account number does not exist',
                        })
                    } else{
                        Swal.fire({
                            icon: 'question',
                            title: 'Data incomplete',
                            text: 'Please, check that all fields are completed',
                        })
                    }
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
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html')
        }      
    },
    computed: {
        filterAccount() {
            this.excludeAccounts = this.accountsNumber.filter(account => account != this.accountOrigin)
        }
    }
})

app.mount('#content')