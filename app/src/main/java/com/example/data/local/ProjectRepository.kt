package com.example.data.local

import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val dao: ProjectDao) {
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
    val allClips: Flow<List<ClipEntity>> = dao.getAllClips()

    fun getClipsForProject(projectId: String): Flow<List<ClipEntity>> {
        return dao.getClipsForProject(projectId)
    }

    suspend fun getProjectById(id: String): ProjectEntity? {
        return dao.getProjectById(id)
    }

    suspend fun saveProject(project: ProjectEntity, clips: List<ClipEntity>) {
        dao.insertProject(project)
        dao.insertClips(clips)
    }

    suspend fun markExported(clipId: String) {
        dao.markClipExported(clipId)
    }

    suspend fun deleteProject(projectId: String) {
        dao.deleteProject(projectId)
    }

    suspend fun deleteClip(clipId: String) {
        dao.deleteClip(clipId)
    }
}
