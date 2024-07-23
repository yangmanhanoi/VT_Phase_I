import axios from "axios";

const getUserDetail = (id) => {
    return axios.create({
        baseURL: `https://graph.facebook.com/v20.0/${id}?fields=id,name,first_name,last_name,profile_pic&access_token=EAAGZA4ChIhZAkBO8MDbLgJ3R0KaODhU5me2U8JthusKEcaSLbYqlEko1e9T6xguxMZBWMVKS4y5guHMlLJN0MJlmIQrdG7CK3L2ZCH6X0q1kstDD8DUBmcktc41wQtGHwJkXgZCAZCwodDfqoCtAHzt7zB9HDpe60CZAkCbCfNNl5EUTyvGuq5FpihahChgQTIZBZBZBO9rY0nibZBGH8FJ2gKbWSMeiAZDZD`
    })
}
export default getUserDetail;