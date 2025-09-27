<template>
  <v-container fill-height justify-center>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-card min-width="350" class="mx-2">
        <v-card-text>
          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="E-mail"
            outlined
            required
          ></v-text-field>

          <v-text-field
            v-model="password"
            :rules="passwordRules"
            label="Password"
            type="password"
            outlined
            required
            @keypress.enter="submit"
          ></v-text-field>

          <v-card-actions>
            <v-btn text @click="signUp"> Sign Up </v-btn> 
            <v-spacer/>
            <v-btn
              :disabled="!valid"
              color="primary dark"
              @click="submit"
            >
              Login
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>
    </v-form>
  </v-container>
</template>

<script>
//import axios from 'axios'
import api from '../axios'

export default {
  data: () => ({
    valid: true,
    password: '',
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
    //email: '',
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ]
  }),
  computed: {
    email: {
      get() {
        return this.$store.state.userId
      },
      set(value) {
        this.$store.commit("setUserId", value)
      }
    }
  },
  methods: {
    signUp() {
      this.$router.push('/signup')
    },
    validate() {
      this.$refs.form.validate();
    },
    async submit() {
      this.validate()

      const data = {
        "email": this.email,
        "password": this.password
      }

      const header = {
        'Content-Type': 'application/json'
      }

      try {
        const response = await api.post('/api/v1/auth/login', data, header)
        const token = response.data.accessToken;
        this.$store.commit("setAccessToken", token)
        this.$router.push("/register-item")
      } catch(error) {
        if (error.response) {
            alert(error.response.data.error)
            console.log(error.response.data.message)
        }
        console.warn("Error: ", error);
      }
    }
  },
  mounted() {
    this.$store.commit('setShowBottomNavigation', false)
  },
};
</script>
