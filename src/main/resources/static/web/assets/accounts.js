const {createApp} = Vue

const app = createApp({
    data() {
        return {
            data: [],
            dataAccounts: [],
            dataLoans: [],
            loanAmount: [],
            addAmount: undefined,
            balanceLoan: [], 
            typeAccount: '',
            account: [],
            show_account: true
        }
    },
    created() {
        this.loadClient()
    },
    methods: {
        loadClient() {
            axios.get("/api/clients/current")
                .then(result => {
                    this.balanceLoan = result.data.accounts
                    this.data = result.data
                    this.dataAccounts = this.data.accounts
                    console.log(this.dataAccounts);
                    this.dataAccounts.sort((a,b) => {
                        if(a.id < b.id) {
                            return -1
                        }
                    }) 
                    this.dataLoans = this.data.loans
                    this.loadAmound(this.dataLoans)
                })
                .catch(error => console.error(error))
        },
        loadAmound(loans) {
            if(loans.length) {
                this.loanAmount = loans.map(a => a.amount)
                this.addAmount = this.loanAmount.reduce((a, b) => a + b)
            }
        },
        addAccount() {
            axios.post("/api/clients/current/accounts", `accountType=${this.typeAccount}`)
                .then(() => location.href = "/web/accounts.html")
        }, 
        deleteAccount(id) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
              }).then((result) => {
                if (result.isConfirmed) {
                  Swal.fire(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success',
                    axios.post(`/api/clients/current/accounts/${id}`)
                    .then(()=> {
                        this.account = this.dataAccounts.filter(account => account.id == id)
                        this.show_account = this.account[0].showAccount
                        if (this.show_account){
                            this.show_account=false
                        }
                        location.href = "/web/accounts.html"
                    })
                    .catch(() => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: "You can't delete an account with a balance",
                        })
                    })
                  )
                }
              })
        },
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html'  )
        }      
    }
})

app.mount('#content')