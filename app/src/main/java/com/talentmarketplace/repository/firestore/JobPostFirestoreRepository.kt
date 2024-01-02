package com.talentmarketplace.repository.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class JobPostFirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore,
): JobPostRepository {
    private val jobPostsCollection = db.collection("jobPosts")

    override suspend fun createJobPost(jobPost: JobPostModel) {
        try {
            val id = jobPostsCollection
                .document()
                .id

            jobPostsCollection
                .document(id)
                .set(jobPost.copy(jobPostID = id))
                .await()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
        }
    }

    override suspend fun getJobPostByID(jobPostID: String): JobPostModel? {
        return try {
            jobPostsCollection
                .document(jobPostID)
                .get()
                .await()
                .toObject<JobPostModel>()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getJobPosts(): List<JobPostModel> {
        return try {
            jobPostsCollection
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject<JobPostModel>() }
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun deleteJobPost(jobPostID: String) {
        try {
            jobPostsCollection
                .document(jobPostID)
                .delete()
                .await()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteJobPosts() {
        try {
            jobPostsCollection
                .document()
                .delete()
                .await()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
        }
    }


    override suspend fun updateJobPost(updatedJobPost: JobPostModel) {
        try {
            jobPostsCollection
                .document(updatedJobPost.jobPostID)
                .set(updatedJobPost)
                .await()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
        }
    }

    override suspend fun getJobPostsByUserID(userID: String): List<JobPostModel> {
        return try {
            jobPostsCollection
                .whereEqualTo("userID", userID)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject<JobPostModel>() }
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            emptyList()
        }
    }
}
