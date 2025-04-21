import axios from 'axios';
import { API_BASE_URL } from '../config/constants';

interface CreateGroupRequest {
    name: string;
    isPublic: boolean;
}

interface UpdateGroupRequest {
    name: string;
}

interface Group {
    id: number;
    name: string;
    admin: {
        id: number;
        fullName: string;
    };
    members: Array<{
        id: number;
        fullName: string;
    }>;
    posts: Array<any>;
    createdDate: string;
}

class GroupService {
    private getHeaders() {
        const token = localStorage.getItem('token');
        return {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
        };
    }

    async createGroup(request: CreateGroupRequest): Promise<Group> {
        try {
            const response = await axios.post(`${API_BASE_URL}/api/groups`, request, {
                headers: this.getHeaders(),
            });
            return response.data.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async updateGroup(groupId: number, request: UpdateGroupRequest): Promise<Group> {
        try {
            const response = await axios.put(
                `${API_BASE_URL}/api/groups/${groupId}`,
                request,
                { headers: this.getHeaders() }
            );
            return response.data.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async deleteGroup(groupId: number): Promise<void> {
        try {
            await axios.delete(`${API_BASE_URL}/api/groups/${groupId}`, {
                headers: this.getHeaders(),
            });
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async joinGroup(groupId: number): Promise<void> {
        try {
            await axios.post(
                `${API_BASE_URL}/api/groups/${groupId}/join`,
                {},
                { headers: this.getHeaders() }
            );
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async leaveGroup(groupId: number): Promise<void> {
        try {
            await axios.post(
                `${API_BASE_URL}/api/groups/${groupId}/leave`,
                {},
                { headers: this.getHeaders() }
            );
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async acceptJoinRequest(groupId: number, userId: number): Promise<void> {
        try {
            await axios.post(
                `${API_BASE_URL}/api/groups/${groupId}/accept-request/${userId}`,
                {},
                { headers: this.getHeaders() }
            );
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async rejectJoinRequest(groupId: number, userId: number): Promise<void> {
        try {
            await axios.post(
                `${API_BASE_URL}/api/groups/${groupId}/reject-request/${userId}`,
                {},
                { headers: this.getHeaders() }
            );
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async getPendingRequests(groupId: number): Promise<Array<{ id: number; fullName: string }>> {
        try {
            const response = await axios.get(
                `${API_BASE_URL}/api/groups/${groupId}/pending-requests`,
                { headers: this.getHeaders() }
            );
            return response.data.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async getAllGroups(): Promise<Group[]> {
        try {
            const response = await axios.get(`${API_BASE_URL}/api/groups`, {
                headers: this.getHeaders(),
            });
            return response.data.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async getGroupById(groupId: number): Promise<Group> {
        try {
            const response = await axios.get(`${API_BASE_URL}/api/groups/${groupId}`, {
                headers: this.getHeaders(),
            });
            return response.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async getMyGroups(): Promise<Group[]> {
        try {
            const response = await axios.get(`${API_BASE_URL}/api/groups/my-groups`, {
                headers: this.getHeaders(),
            });
            return response.data.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    private handleError(error: any): Error {
        if (error.response) {
            const message = error.response.data.message || 'An error occurred';
            return new Error(message);
        }
        return new Error('Network error occurred');
    }
}

export const groupService = new GroupService(); 