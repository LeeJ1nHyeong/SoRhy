// 댓글 작성 post

import api from "../api";
import dotenv from 'dotenv'
require('dotenv').config()
const articleCommentPost = async (articleId: any, commentDatas: any) => {
    try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await api.post(`/${articleId}/comment`, commentDatas, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        return response.data.response;
    } catch (error) {
        console.error("Error: ", error);
        throw error;
    }
};

export default articleCommentPost;
