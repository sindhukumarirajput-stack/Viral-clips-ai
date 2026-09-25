package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String)

    @Query("SELECT * FROM clips WHERE projectId = :projectId ORDER BY viralityScore DESC")
    fun getClipsForProject(projectId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips ORDER BY createdAt DESC")
    fun getAllClips(): Flow<List<ClipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClips(clips: List<ClipEntity>)

    @Query("UPDATE clips SET isExported = 1 WHERE id = :clipId")
    suspend fun markClipExported(clipId: String)

    @Query("DELETE FROM clips WHERE id = :clipId")
    suspend fun deleteClip(clipId: String)
}
