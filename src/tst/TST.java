package tst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TST {

	private final char EMPTY = 9999; 
	private Node root;
	
	public TST(){
		root = new Node();
	}
	
	public void insert(String s, long w){
		insert(s, w, root, s);
	}
	
	private void insert(String s, long w, Node n, String word){
		//get first character
		char first = s.charAt(0);
		
		//if node val is empty, put first char there 
		if(n.val == EMPTY){
			n.val = first;
		}
		
		//if maxSubWeight < weight, make max = weight
		if(n.maxSubWeight < w){
			n.maxSubWeight = w;
		}		

		if(first==n.val){
			if(s.length() > 1){
				//if node val matches first character and s.length > 1, 
				//insert tail of s in middle
				if(n.middle == null)
					n.middle = new Node();
				insert(s.substring(1), w, n.middle, word);
			} else {
				//if s is length==1, this is the last character
				//if val = first  and weight is > 0, then this word has been inserted
				//already, throw exception
				//otherwise, set val = char and weight = weight
				//if(n.weight > 0)
					//throw new IllegalArgumentException("Cannot insert duplicate string");
				n.weight = w;
				n.word = word;
			}
		} else if(first < n.val ){
			//if node val is greater than first character, 
			//get left node
			//if left is null, make it a new node
			//insert whole string into left
			if(n.left == null)
				n.left = new Node();
			insert(s, w, n.left, word);			
		} else {
			//otherwise do the whole process for right
			if(n.right == null)
				n.right = new Node();
			insert(s, w, n.right, word);
		}
	}
	
	public long find(String s){
		Node n = findNode(s, root);
		if(n == null)
			return 0;
		return n.weight;
		
	}

	private Node findNode(String s, Node n){
		if(s.length() < 1)
			return null;
		
		//get first character
		char first = s.charAt(0);
		
		if (n==null)
			return null;
		//||(String.valueOf(first).matches("[a-zA-Z]+")==true && String.valueOf(first).toLowerCase().equals(String.valueOf(n.val).toLowerCase()))
		if(first == n.val){
			if(s.length() > 1){
				//if node val matches first character and s.length > 1, 
				//keep looking down middle
				return findNode(s.substring(1), n.middle);
			} else {
				//if s is length==1, this is the last character
				//return the weight
				return n;
			}
		} else if(first < n.val){
			//if node val is greater than first character, 
			//get left node
			//if left is null, string is not in trie, return 0
			//otherwise, look in left for whole string
			if(n.left == null)
				return null;
			return findNode(s, n.left);			
		} else {
			//otherwise do the whole process for right
			if(n.right == null)
				return null;
			return findNode(s, n.right);
		}
	}
	
	private List<Node> findMatches(String prefix, int k){
		
		if(k <= 0)
			throw new IllegalArgumentException("Cannot find non-positive number of matches");
		
		
		PriorityQueue<Node> bestAnswer = new PriorityQueue<Node>(10, new MinNodeComparator());
		PriorityQueue<Node> pq = new PriorityQueue<Node>(10, new MaxNodeComparator());
		List<Node> answers = new ArrayList<Node>(10);
		
		//find prefix root node
		Node x = findNode(prefix, root);
		
		if(x == null){
			answers.add(x);
			return answers;
		}
		//if middle is null, this is the only match, just return it
		if(x.middle == null){
			if(x.word == null){
				answers.add(x);
				return answers;
			}
			else{
				answers.add(x);
				return answers;
			}
		}
		
		long bestWeight = x.middle.maxSubWeight;
		boolean bestFound = false;
		pq.add(x.middle);
		
		while(pq.size() > 0){
			Node current = pq.poll();
			
			if(current.weight > 0){				
				bestAnswer.add(current);
				
				//only keep the k best
				while(bestAnswer.size() > k)
					bestAnswer.poll();
			}
				
			
			if(current.weight == bestWeight)
				bestFound = true;
			
			enqueueChildren(current, pq);
			
			//check if we can end search early...
			if(bestFound 
					&& bestAnswer.size() >= k 
					&& pq.size() > 0 
					&& bestAnswer.peek().weight > pq.peek().maxSubWeight){
				//System.out.println("Completed search early!");
				break;
			}
				
		}
		
		Node[] nodes = new Node[bestAnswer.size()];
		for(int i = 0; i < nodes.length; i++)
			nodes[i] = bestAnswer.poll();	
		
		for(int i = 0; i < nodes.length && i < k; i++){
			int index = nodes.length - 1 - i;
			answers.add(nodes[index]);
		}
		
		return answers;
	}
	
	private List<String> findMatchesToString(String prefix, int k){
		List<String> result=new ArrayList<String>(2*k);
		List<Node> answer=new ArrayList<Node>(2*k);
		answer=findMatches(prefix,k);
		if (answer!=null){
			for (int i=0;i<answer.size()&&i<k;++i)
			{
				if (answer.get(i)!=null){
				result.add(answer.get(i).word);
				result.add(String.valueOf(answer.get(i).weight));
				}
				else 
					return null;
			}
			return result;
		}
		else 
			return null;
	}
	
	public List<String> find(String prefix, int k){
		List<String> result=new ArrayList<String>(100);
		List<String> temp1=new ArrayList<String>(100);
		temp1=findMatchesToString(prefix, k);
		if (temp1!=null){
			result.addAll(temp1);
		}
		if (prefix.length()>4){
			for (int i=1;i<prefix.length()-1;++i){
				List<String> temp2=new ArrayList<String>(100);
				temp2=findMatchesToString((prefix.substring(0, i)+prefix.substring(i+1)), k);
				if (temp2!=null){	
					result.addAll(temp2);
				}
			}
		}
		if (prefix.length()>4){
			for (int i=1;i<prefix.length()-2;++i){
				List<String> temp3=new ArrayList<String>(100);
				temp3=findMatchesToString(prefix.substring(0,i)+prefix.substring(i+1,i+2)+prefix.substring(i,i+1)+prefix.substring(i+2),k);
				if (temp3!=null){	
					result.addAll(temp3);
				}
			}
		}
		int space=0;
		int m=0;
		for (int i=0;i<prefix.length();++i){
			if (prefix.charAt(i)==' '){
				space++;
				if (m==0){m=i;}
			}
		}
		String[] alphabet={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		if (space==1){
			List<String> temp4=new ArrayList<String>(100);
			for (int i=0;i<26;++i){
				temp4=findMatchesToString(prefix.substring(0,m)+" "+alphabet[i]+" "+prefix.substring(m+1),k);
				if (temp4!=null){	
					result.addAll(temp4);
				}
			}
		}
		if (result.size()==0){
			return null;
		}
		
			int total_result=result.size();
			ArrayList<String> final_result=new ArrayList<String>(total_result);
			ArrayList<String> final_result_weight=new ArrayList<String>(total_result);
			final_result.add(result.get(0));
			final_result_weight.add(result.get(1));
			int i,j;
			for (i=2;i<total_result-1;++i){
				for (j=0;j<i/2;++j){
					if (Integer.parseInt(result.get(i+1))>Integer.parseInt(final_result_weight.get(j))){
						break;
					}
					
				}
				final_result.add(j,result.get(i));
				final_result_weight.add(j,result.get(i+1));
				++i;
			}
			for (i=0;i<final_result.size()-1;i++){
				String temp=final_result.get(i);
				for (j=final_result.size()-1;j>i;j--){
					if (temp==final_result.get(j)){
						final_result.remove(j);
						final_result_weight.remove(j);
					}
				}
			}
			return final_result;
	}
	
	public List<String> find_authors(String prefix, int k){
		List<String> result=new ArrayList<String>(100);
		List<String> temp1=new ArrayList<String>(100);
		temp1=findMatchesToString(prefix, k);
		if (temp1!=null){
			result.addAll(temp1);
		}
		if (prefix.length()>4){
			for (int i=1;i<prefix.length()-1;++i){
				List<String> temp2=new ArrayList<String>(100);
				temp2=findMatchesToString((prefix.substring(0, i)+prefix.substring(i+1)), k);
				if (temp2!=null){	
					result.addAll(temp2);
				}
			}
		}
		if (prefix.length()>4){
			for (int i=1;i<prefix.length()-2;++i){
				List<String> temp3=new ArrayList<String>(100);
				temp3=findMatchesToString(prefix.substring(0,i)+prefix.substring(i+1,i+2)+prefix.substring(i,i+1)+prefix.substring(i+2),k);
				if (temp3!=null){	
					result.addAll(temp3);
				}
			}
		}
		int space=0;
		int m=0;
		for (int i=0;i<prefix.length();++i){
			if (prefix.charAt(i)==' '){
				space++;
				if (m==0){m=i;}
			}
		}
		String[] alphabet={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		if (space==1){
			List<String> temp4=new ArrayList<String>(100);
			for (int i=0;i<26;++i){
				temp4=findMatchesToString(prefix.substring(0,m)+" "+alphabet[i]+" "+prefix.substring(m+1),k);
				if (temp4!=null){	
					result.addAll(temp4);
				}
			}
		}
		if (result.size()==0){
			return null;
		}
		
			int total_result=result.size();
			ArrayList<String> final_result=new ArrayList<String>(total_result);
			ArrayList<String> final_result_weight=new ArrayList<String>(total_result);
			final_result.add(result.get(0));
			final_result_weight.add(result.get(1));
			int i,j;
			for (i=2;i<total_result-1;++i){
				for (j=0;j<i/2;++j){
					if (Integer.parseInt(result.get(i+1))>Integer.parseInt(final_result_weight.get(j))){
						break;
					}
					
				}
				final_result.add(j,result.get(i));
				final_result_weight.add(j,result.get(i+1));
				++i;
			}
			for (i=0;i<final_result.size()-1;i++){
				String temp=final_result.get(i);
				for (j=final_result.size()-1;j>i;j--){
					if (temp==final_result.get(j)){
						final_result.remove(j);
						final_result_weight.remove(j);
					}
				}
			}
			final_result.addAll(final_result_weight);
			return final_result;
		
	}
	
	private void enqueueChildren(Node x, PriorityQueue<Node> pq) {
		if(x.left != null)
			pq.add(x.left);
		if(x.middle != null)
			pq.add(x.middle);
		if(x.right != null)
			pq.add(x.right);
	}

	public class MaxNodeComparator implements Comparator<Node>{
        public int compare(Node e1, Node e2)
        {
            return (int)(e2.maxSubWeight - e1.maxSubWeight);
        }
    }
	
	public class MinNodeComparator implements Comparator<Node>{
        public int compare(Node e1, Node e2)
        {
            return (int)(e1.weight - e2.weight);
        }
    }
	
	public class Node{
		public Node right;
		public Node left;
		public Node middle;
		public char val = EMPTY;
		public long weight;
		public long maxSubWeight;	
		public String word;
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(val + " ");
			if(middle != null)
				sb.append(middle.toString());
			if(left != null)
				sb.append(left.toString());
			if(right != null)
				sb.append(right.toString());
			return sb.toString();
		}
	}
	
	@Override
	public String toString(){
		return root.toString().trim();
	}
	
	 
}


